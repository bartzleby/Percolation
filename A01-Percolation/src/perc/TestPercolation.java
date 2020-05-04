package perc;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestPercolation {
	
	Percolation p = new Percolation(9);

	@Test
	public void test() {
		assertFalse(p.percolates());
	}

}
